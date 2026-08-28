Add-Type -TypeDefinition @"
  using System;
  using System.Runtime.InteropServices;
  
  public class Win32 {
    [DllImport("user32.dll")]
    public static extern bool GetWindowRect(IntPtr hWnd, out RECT lpRect);
    
    [StructLayout(LayoutKind.Sequential)]
    public struct RECT {
      public int Left;
      public int Top;
      public int Right;
      public int Bottom;
    }
  }
"@

Add-Type -AssemblyName System.Windows.Forms, System.Drawing

$ProcessName = "java"
$WindowTitle = "Java3D Car Debug"

# Find process with matching title
$Proc = Get-Process | Where-Object { $_.MainWindowTitle -eq $WindowTitle } | Select-Object -First 1

if ($null -eq $Proc) {
    Write-Host "Window '$WindowTitle' not found via Get-Process. Checking all java processes..."
    Get-Process java | ForEach-Object { Write-Host "Java Process: ID=$($_.Id), Title='$($_.MainWindowTitle)'" }
    
    Write-Host "Capturing primary screen as fallback."
    $Screen = [System.Windows.Forms.Screen]::PrimaryScreen
    $Rect = New-Object Win32+RECT
    $Rect.Left = $Screen.Bounds.X
    $Rect.Top = $Screen.Bounds.Y
    $Rect.Right = $Screen.Bounds.X + $Screen.Bounds.Width
    $Rect.Bottom = $Screen.Bounds.Y + $Screen.Bounds.Height
} else {
    $hWnd = $Proc.MainWindowHandle
    $Rect = [Win32+RECT]::new()
    [void][Win32]::GetWindowRect($hWnd, [ref]$Rect)
    Write-Host "Found Window '$WindowTitle' (PID $($Proc.Id)). Capturing at ($($Rect.Left), $($Rect.Top))"
}

$Width = $Rect.Right - $Rect.Left
$Height = $Rect.Bottom - $Rect.Top

if ($Width -le 0 -or $Height -le 0) {
    Write-Error "Invalid window dimensions ($Width x $Height)."
    exit
}

# Add some padding removal if window is maximized (often extends beyond screen by 8px)
# But strictly speaking, we just want the rect.
$Bitmap = New-Object System.Drawing.Bitmap $Width, $Height
$Graphics = [System.Drawing.Graphics]::FromImage($Bitmap)
$Graphics.CopyFromScreen($Rect.Left, $Rect.Top, 0, 0, $Bitmap.Size)

$OutputPath = "c:\repos\va\java3d-car\latest_screenshot.png"
$Bitmap.Save($OutputPath, [System.Drawing.Imaging.ImageFormat]::Png)

$Graphics.Dispose()
$Bitmap.Dispose()

Write-Host "Screenshot saved to $OutputPath"
