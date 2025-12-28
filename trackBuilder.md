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
