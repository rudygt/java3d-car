# TrackBuilder Documentation

`TrackBuilder` is a fluent API for creating race tracks in the 3D car simulator. It abstracts the complexity of 3D transformations, allowing you to chain segments together naturally.

## Overview

The `TrackBuilder` maintains a "cursor" (a `Transform3D`) which represents the end of the last added segment. Every time you add a segment, it is placed at the cursor's position and orientation, and the cursor is then advanced to the end of that new segment, ready for the next one.

## Usage

```java
import test.TrackBuilder;
import org.scijava.java3d.Node;

public class MyTrack {
    public static Node build() {
        return new TrackBuilder()
            .straight(50)
            .curveRight()
            .hill(10, 50)
            .curveLeft()
            .build();
    }
}
```

## Available Segments (Building Blocks)

All segments are added relative to the current cursor position.

### 1. Straight
Adds a straight road segment.
- **Method:** `straight(double length)`
- **Parameters:**
  - `length`: The length of the segment in world units.
- **Behavior:** Extends along the current X-axis.

### 2. Curves (Flat)
Adds a 90-degree or custom angle curve.
- **Methods:**
  - `curveRight()`: 90-degree right turn.
  - `curveRight(double angle)`: Right turn with custom angle (radians).
  - `curveLeft()`: 90-degree left turn.
  - `curveLeft(double angle)`: Left turn with custom angle (radians).
- **Behavior:** Turns the track left or right. The radius is fixed at 10.0 units.

### 3. Curves (With Height)
Adds a curve that also rises or falls in height (like a ramp in a turn).
- **Methods:**
  - `curveRightWithHeight(double angle, int height)`
  - `curveLeftWithHeight(double angle, int height)`
- **Parameters:**
  - `angle`: The angle of the turn in radians.
  - `height`: The change in height (Y-axis) over the curve. Positive is up, negative is down.

### 4. Hill (Slope)
Adds a straight segment that changes height.
- **Method:** `hill(double height, int length)`
- **Parameters:**
  - `height`: The total height change.
  - `length`: The horizontal length of the slope.
- **Behavior:** Creates a ramp.

### 5. Helix (Caracol)
Adds a 360-degree spiral loop.
- **Method:** `helix(int height)`
- **Parameters:**
  - `height`: The scaling factor for the height of the spiral. The total height change will be `height * 5`.
- **Behavior:** A full loop that gains significant altitude.

## Helper Methods

### Rotate
Manually rotates the cursor without adding geometry.
- **Method:** `rotate(double angle)`
- **Parameters:**
  - `angle`: Rotation angle in radians around the Y-axis.

### Translate
Manually moves the cursor without adding geometry.
- **Method:** `translate(double x, double y, double z)`
- **Parameters:**
  - `x, y, z`: Translation vector.

## Low-Level Geometry: How Segments Are Built

This section documents how `TrackBuilder` ultimately creates geometry via
`MapBuilder` and Java3D primitives.

### Core Primitive: `MapBuilder.getSegmento()`
- **File:** `src/main/java/test/MapBuilder.java`
- **Purpose:** Build one track "slice" (a short, extruded road section).
- **Primitive:** `QuadArray` with 8 quads (32 vertices) representing the road
  surface and side faces.
- **Shape profile:** `puntosBase` defines 8 points that describe a road cross
  section (width, height, and a thin edge lip). These points are transformed
  twice:
  - `actual` is the transform for the slice start.
  - `siguiente` is the transform for the slice end.
- **Geometry assembly:** The method builds 8 quads by connecting each edge of
  the start profile (`ladoA`) to the matching edge of the end profile (`ladoB`).
- **Normals/UVs:** `NormalGenerator` computes normals; texture coordinates are
  assigned per face using `TexCoord2f` constants.
- **Appearance:** `Tools.cargarTextura("c:\\3d\\output.png")` is loaded once and
  reused (static `appPista`).

### Straight Segments: `getSegmentoRecto(double)`
- Builds a `Group` of consecutive slices.
- `actual` starts at identity.
- For each unit step `i`, `siguiente` translates by `(i, 0, 0)`, and
  `getSegmento()` bridges `actual` -> `siguiente`.
- A fractional remainder (if any) adds one more slice at `pLongitud`.

### Slopes: `getPendiente(double height, int length)`
- Same slice loop as straight, but `siguiente` translates by `(i, i*pasoAltura, 0)`.
- `pasoAltura = height / length` creates a linear ramp in Y.

### Curves: `getCurvaDerecha/Izquierda(double angle)`
- Splits the turn into `numSegmentos = (angle / (PI/2)) * 15` slices.
- Each slice computes an arc point:
  - Right: `x = sin(angle)*radius`, `z = -cos(angle)*radius`, with `radius = 10`.
  - Left: same math, but mirrored by starting center `(0, -radius)` and
    opposite rotation.
- `siguiente` applies translation to the arc point and a Y-rotation to align
  the slice tangent to the curve.

### Curves With Height: `getCurvaDerecha/Izquierda(double, int height)`
- Adds vertical progression `pasoAltura = height / numSegmentos` on each slice.
- Uses the same arc math and tangent rotation as flat curves.

### Helix: `getCaracol(int height)`
- Builds a spiral by stepping through `numSegmentos = 60 * abs(height)` slices.
- Uses a larger turn radius (`radioGiro = 15`) and full rotations
  (`maxAngle = abs(height) * 2*PI`).
- Each slice advances:
  - X/Z along the circle.
  - Y by `dy = (height * 5) / numSegmentos`.

### How `TrackBuilder` Chains Segments
- `TrackBuilder.add(...)` places each segment `Node` under a `TransformGroup`
  with the current cursor transform, then advances the cursor by a computed
  `delta` transform.
- This decouples segment geometry (built in `MapBuilder`) from placement and
  chaining in the final track.
