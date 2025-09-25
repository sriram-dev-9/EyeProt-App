# 👁️ EyeProt - Digital Eye Guardian

A lightweight Java desktop application that helps prevent digital eye strain by implementing the scientifically-backed **20-20-20 rule**: Every 20 minutes, look at something 20 feet away for at least 20 seconds.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

## ✨ Features

- 🖥️ **System Tray Integration** - Runs silently in the background
- 🌊 **Beautiful Overlays** - Semi-transparent, non-intrusive notifications
- ⏰ **Precise Timing** - Automatic 20-minute work intervals
- 🎨 **Custom Graphics** - Hand-drawn icons with smooth animations
- 🪶 **Lightweight** - <1MB memory footprint, 0% CPU when idle
- ⚙️ **Simple Controls** - Right-click menu with start/pause/test options
- 🔄 **Cross-Platform** - Works on Windows, macOS, and Linux

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher
- Operating system with system tray support

### Installation & Usage

1. **Download** the latest release or clone this repository
```bash
git clone https://github.com/sriram-dev-9/EyeProt-App.git
cd eyeprot
```

2. **Compile** the application
```bash
javac EyeProtApp.java
```

3. **Run** EyeProt
```bash
java EyeProtApp
```

4. **Look for the eye icon** in your system tray - EyeProt is now protecting your vision! 👁️

## 🎯 How It Works

1. **Silent Guardian** - EyeProt sits quietly in your system tray
2. **Smart Reminders** - Every 20 minutes, a gentle overlay appears
3. **Guided Breaks** - Follow the countdown timer for your 20-second break
4. **Auto-Dismiss** - Notification fades away automatically or click "Dismiss"
5. **Repeat** - The cycle continues to keep your eyes healthy

## 🛠️ System Tray Controls

Right-click the EyeProt icon in your system tray to access:

- **Start Timer** - Begin the 20-minute work intervals
- **Pause Timer** - Temporarily stop notifications
- **Test Notification** - Preview the break reminder
- **About** - Learn more about the 20-20-20 rule
- **Exit** - Close the application

## 🏗️ Technical Architecture

### Core Components
- **SystemTray Integration** - Native OS tray icon with popup menu
- **Timer Management** - Precise interval scheduling with Java Timer
- **Custom Overlay System** - Semi-transparent JWindow notifications
- **Graphics Rendering** - Hand-drawn icons using Graphics2D
- **Thread Safety** - Proper UI updates with SwingUtilities

### Key Technologies
- **Java Swing/AWT** - Desktop GUI framework
- **Graphics2D** - Custom icon rendering
- **SystemTray API** - Background operation
- **Timer/TimerTask** - Precise scheduling

## 🎨 Screenshots

### System Tray Icon
The subtle eye icon sits in your system tray:

### Notification Overlay
Beautiful, calming reminder that doesn't break your focus:
- Semi-transparent background
- Clear instructions
- Live countdown timer
- Easy dismiss button

## 🔧 Customization

Want to modify the intervals or appearance? Key constants in `EyeProtApp.java`:

```java
private static final int WORK_INTERVAL = 20 * 60 * 1000; // 20 minutes
private static final int REST_DURATION = 20 * 1000;      // 20 seconds
```

Overlay colors can be adjusted in the `createNotificationWindow()` method.


## 🤝 Contributing

Contributions are welcome! Whether it's:
- 🐛 Bug fixes
- ✨ New features
- 📖 Documentation improvements
- 🎨 UI/UX enhancements

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built during **Hack Club's Summer of Making**
- Inspired by the scientific research on digital eye strain
- Thanks to the ophthalmologists who discovered the 20-20-20 rule

---