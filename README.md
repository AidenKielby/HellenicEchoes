# Hellenic Echoes

A Minecraft mod that brings ancient Greek-inspired content to your world, including bronze crafting, mystical aether crystals, and custom alloy forging mechanics.

## 📋 Description

**Hellenic Echoes** is a Minecraft Forge mod for version 1.20.1 that adds Greek mythology-themed items, blocks, and gameplay mechanics. Craft bronze tools and armor, discover rare aether crystals, and use the custom Alloy Forge to create powerful alloys.

## ✨ Features

### Items
- **Aether Crystal** - A mystical crystal with powerful properties
- **Aether Shard** - Fragments of aether crystals
- **Bronze** - A copper-tin alloy material

### Armor
- **Bronze Armor Set** - Complete armor set including:
  - Bronze Helmet
  - Bronze Chestplate
  - Bronze Leggings
  - Bronze Boots

### Blocks
- **Bronze Ore** - Naturally spawning ore that can be mined
- **Aether Ore** - Rare mystical ore that drops experience when mined
- **Alloy Forge** - Custom crafting station for creating alloys

### Mechanics
- **Alloy Forging System** - Use the Alloy Forge to combine materials and create bronze from iron and copper
- **Custom Recipes** - Unique crafting recipes for bronze and alloy creation

## 🎮 Installation (For Players)

### Using the Minecraft Launcher

1. **Install Minecraft Forge**
   - Download Forge 47.4.13 for Minecraft 1.20.1 from [files.minecraftforge.net](https://files.minecraftforge.net/)
   - Run the Forge installer and select "Install Client"
   - Launch Minecraft and select the Forge profile

2. **Install Hellenic Echoes**
   - Download the latest release of Hellenic Echoes from the [Releases page](https://github.com/AidenKielby/HellenicEchoes/releases)
   - Locate your Minecraft installation folder:
     - **Windows**: `%APPDATA%\.minecraft`
     - **Mac**: `~/Library/Application Support/minecraft`
     - **Linux**: `~/.minecraft`
   - Place the downloaded `.jar` file in the `mods` folder
   - If the `mods` folder doesn't exist, create it
   - Launch Minecraft with the Forge profile

3. **Verify Installation**
   - Start Minecraft and click on "Mods" in the main menu
   - Look for "Hellenic Echoes" in the mod list
   - You're ready to play!

## 💻 Development Setup (For Modders)

### Prerequisites
- Java Development Kit (JDK) 17
- Git
- IDE of your choice (IntelliJ IDEA or Eclipse recommended)

### Setting Up in IntelliJ IDEA

1. **Clone the Repository**
   ```bash
   git clone https://github.com/AidenKielby/HellenicEchoes.git
   cd HellenicEchoes
   ```

2. **Import Project**
   - Open IntelliJ IDEA
   - Select "Open" and choose the `build.gradle` file
   - Let IntelliJ import the project and download dependencies

3. **Generate Run Configurations**
   ```bash
   ./gradlew genIntellijRuns
   ```
   - On Windows use: `gradlew.bat genIntellijRuns`
   - After completion, refresh the Gradle project in IntelliJ (View → Tool Windows → Gradle → Refresh)

4. **Run the Mod**
   - Look for the run configurations in the top-right corner of IntelliJ
   - Select "runClient" to run Minecraft with the mod loaded
   - Select "runServer" to run a dedicated server with the mod
   - Click the green play button to start

### Setting Up in Eclipse

1. **Clone the Repository**
   ```bash
   git clone https://github.com/AidenKielby/HellenicEchoes.git
   cd HellenicEchoes
   ```

2. **Generate Eclipse Project**
   ```bash
   ./gradlew genEclipseRuns
   ./gradlew eclipse
   ```
   - On Windows use: `gradlew.bat genEclipseRuns` and `gradlew.bat eclipse`

3. **Import into Eclipse**
   - Open Eclipse
   - File → Import → Existing Gradle Project
   - Select the HellenicEchoes folder
   - Click Finish

4. **Run the Mod**
   - Right-click on the project
   - Run As → Java Application
   - Select "runClient" or "runServer"

### Common Development Commands

```bash
# Build the mod
./gradlew build

# Run the client (alternative to IDE run configs)
./gradlew runClient

# Run the server
./gradlew runServer

# Generate data (recipes, models, etc.)
./gradlew runData

# Clean build files
./gradlew clean

# Refresh dependencies if you encounter issues
./gradlew --refresh-dependencies
```

## 🔨 Building the Mod

To build the mod for distribution:

```bash
./gradlew build
```

The compiled `.jar` file will be located in:
```
build/libs/hellenicechoes-1.0.0.jar
```

This jar file can be distributed and installed in the `mods` folder of any Minecraft installation with Forge 47.4.13 or compatible versions.

## 📁 Project Structure

```
HellenicEchoes/
├── src/
│   ├── main/
│   │   ├── java/com/aiden/hellenic_echoes/
│   │   │   ├── block/          # Block definitions
│   │   │   ├── item/           # Item definitions
│   │   │   ├── recipe/         # Custom recipes
│   │   │   ├── screen/         # GUI screens
│   │   │   └── HellenicEchoes.java  # Main mod class
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── mods.toml   # Mod metadata
│   │       ├── assets/         # Textures, models, sounds
│   │       └── data/           # Recipes, loot tables, tags
│   └── generated/              # Auto-generated resources
├── build.gradle                # Build configuration
├── gradle.properties           # Mod properties and versions
└── README.md                   # This file
```

## 🐛 Troubleshooting

### Common Issues

**Problem**: "Missing dependencies" error
- **Solution**: Run `./gradlew --refresh-dependencies` and try again

**Problem**: IntelliJ doesn't show run configurations
- **Solution**: 
  1. Run `./gradlew genIntellijRuns` again
  2. Refresh Gradle project
  3. Restart IntelliJ

**Problem**: Eclipse shows compilation errors
- **Solution**:
  1. Right-click project → Gradle → Refresh Gradle Project
  2. Project → Clean
  3. Restart Eclipse

**Problem**: Mod doesn't appear in-game
- **Solution**: 
  1. Verify the mod is in the correct `mods` folder
  2. Check you're using the correct Forge version (47.4.13 for MC 1.20.1)
  3. Check the logs in the `logs` folder for errors

## 📜 Credits

- **Developer**: Aiden Kielbasinski
- **Minecraft Version**: 1.20.1
- **Forge Version**: 47.4.13
- **License**: MIT License

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## 📞 Support

For support, bug reports, or feature requests, please open an issue on the [GitHub repository](https://github.com/AidenKielby/HellenicEchoes/issues).

---

Made with ❤️ for the Minecraft modding community
