# MultiWave

A powerful and customizable multi-layered wave animation header for Android, featuring gradient support and smooth wave effects.

## Installation

### Maven Central
Add the following dependency to your `build.gradle` (Module: app):

```kotlin
dependencies {
    implementation("io.github.selimdawa:multi-wave:x.y.z")
}
```

## Usage

### XML Implementation
Add `MultiWaveHeader` to your layout XML file:

```xml
<io.selimdawa.multiwave.MultiWaveHeader
    android:id="@+id/multiWaveHeader"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    app:mwhWaveHeight="50dp"
    app:mwhStartColor="#FFFA9330"
    app:mwhCloseColor="#FFCE5002"
    app:mwhColorAlpha="0.45"
    app:mwhVelocity="1.0"
    app:mwhGradientAngle="45"
    app:mwhIsRunning="true"
    app:mwhShape="RoundRect"
    app:mwhCornerRadius="25dp"
    app:mwhProgress="0.35"
    app:mwhWaves="MultiWave" />
```

### Attributes Reference

| Attribute             | Description                                              | Default     |
|:----------------------|:---------------------------------------------------------|:------------|
| `mwhWaveHeight`       | The vertical height of the wave peaks.                   | `50dp`      |
| `mwhStartColor`       | The starting color for the wave gradient.                | `#FFFA9330` |
| `mwhCloseColor`       | The ending color for the wave gradient.                  | `#FFCE5002` |
| `mwhColorAlpha`       | Transparency level (0.0 to 1.0).                         | `0.45`      |
| `mwhVelocity`         | Speed multiplier for the wave animation.                 | `1.0`       |
| `mwhGradientAngle`    | Angle (in degrees) for the linear gradient.              | `45`        |
| `mwhIsRunning`        | Whether the animation starts automatically.              | `true`      |
| `mwhEnableFullScreen` | If true, the wave fills the entire view height.          | `false`     |
| `mwhProgress`         | The vertical level of the waves (0.0 to 1.0).            | `0.35`      |
| `mwhShape`            | Clipping shape: `Rect`, `RoundRect`, or `Oval`.          | `RoundRect` |
| `mwhCornerRadius`     | Corner radius for the `RoundRect` shape.                 | `25dp`      |
| `mwhWaves`            | Presets (`MultiWave`, `PairWave`) or custom wave string. | `MultiWave` |

### Custom Waves Configuration
You can define custom wave layers using a string in the following format:
`scaleX, scaleY, velocity, offsetX, offsetY` separated by new lines for multiple layers.

Example:
```xml
app:mwhWaves="70,25,1.4,1.4,-26\n100,5,1.4,1.2,15"
```

## Programmatic Usage (Kotlin)

```kotlin
val multiWaveHeader = findViewById<MultiWaveHeader>(R.id.multiWaveHeader)

// Control animation
multiWaveHeader.start()
multiWaveHeader.stop()

// Update properties
multiWaveHeader.waveHeight = 60
multiWaveHeader.progress = 0.5f
multiWaveHeader.velocity = 1.5f

// Animate progress with duration
multiWaveHeader.setProgress(0.8f, DecelerateInterpolator(), 1000)
```

## License

```
Copyright 2026 Selim Dawa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
