# Build Your Own World Design Document

**Partner 1: Claire Meaney**

**Partner 2: Jordan Klanfer**

## Overview
This project implements a 2D tile-based world engine in Java that allows users to generate, explore, and persist uniquely generated worlds based on user-provided seed values. The engine supports random map generation, interactive gameplay, and game state saving/loading.

## Classes
Engine.java: Entry point for handling user input and controlling the flow of the game.

MapGenerator.java: Responsible for generating the layout of rooms and hallways based on a random seed.

Room.java: Defines room objects and their properties (position, width, height).

TERenderer.java, TETile.java, Tileset.java: Provided tile engine used for rendering the world.

InputSource.java, KeyboardInputSource.java, StringInputDevice.java: Handle input sources for player interaction.

## Data Structures
2D arrays for map representation

List collections for storing and managing generated rooms

Random for seed-based generation

## Algorithms
Room Placement: Randomized room generation algorithm ensures no overlap and varied positioning across the map.

Hallway Generation: Connects rooms using L-shaped or straight paths to ensure full map connectivity.

Player Movement: Processes key inputs to update the player’s location and re-render the view.

Input Parsing: Interprets user commands for both real-time and string-based input sequences.

## Persistence
Game state is serialized and written to a file after user saves or exits the game.

Upon loading, the saved state is deserialized and used to restore the game to its previous state.

Input sequences can also be replayed to regenerate identical game states (deterministic replay).
