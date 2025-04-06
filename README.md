# Breakpoint Counter Plugin for JetBrains IDEs

## Overview  
A JetBrains IDE plugin that displays all project breakpoints in a dedicated tool window, with real-time updates when breakpoints are modified.

---

## Features  
✔ **Real-time monitoring** of breakpoint changes (add/remove/modify)  
✔ Displays **file path** and **line number** for each breakpoint  
✔ **Auto-updating** counter for total breakpoints  
✔ Matches IDE **color scheme** and **font settings**  
✔ Lightweight **embedded server** (Jetty) for JCEF rendering  

---

## Installation  

1. Download the latest `.jar` from [Releases](https://github.com/RandVid/BreakpointCounter/releases)  
2. **Settings/Preferences** → **Plugins** → ⚙️ → **Install Plugin from Disk...**  
3. Select the downloaded file and restart the IDE  

---

## Usage  
1. Open any project  
2. Find the **Breakpoint Counter** on the right bar
3. Interact with breakpoints as usual – the view updates automatically  

---

## Technical Details  
### Architecture  
| Component               | Responsibility                          |
|-------------------------|-----------------------------------------|
| `BreakpointServerManager` | Manages Jetty server (port 15050)      |
| `BreakpointView`         | JCEF browser rendering HTML content    |
| `BreakpointViewFactory`  | Creates the tool window UI             |

### Key Technologies  
- **IntelliJ Platform SDK** (Latest API)  
- **JCEF** (Chromium Embedded Framework)  
- **Jetty** (Embedded HTTP server)  
- `XBreakpointListener` for real-time events  

---

## Build from Source  
```bash
git clone https://github.com/your-repo/breakpoint-counter.git
cd breakpoint-counter
./gradlew build
```

---

## Further Improvements
- Add grouping breakpoints by files
- Implement dynamic ports
