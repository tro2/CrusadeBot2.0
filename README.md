# CrusadeBot2.0 - Java Discord Bot For Basic Server Management

# Overview

`CrusadeBot 2.0` is a discord bot meant for running the Crusade and Paradise Rotmg Discords.
It is based off of `CrusadeBot`, the first discord bot I made.
This iteration utilizes a modular design, and contains functionality for member verification, mutes, raid organization, and other basics.

## Current Status

Finished:
- Basic Headcounts
- Meme Commands

In progress:
- Unix Timestamp Creator

Planned Features:
- Member Greeter
- Guild Join Queue
- Mute Handler
- Interactable Headcounts
- Logging

# Build Instructions

## Requirements

Requires Java 16 or later to run

## How to build

Compile with IntelliJ Idea (Maven project)

Create an Artifact, contents:
- META-INF with MANIFEST.MF
- Extracted compile output of dependencies
- Project compile output

Contents of folder:
- CrusadeBot.db
- config.json
- Resources Folder

## How to Run

Run the .bat file included (if first time running, make sure to update config.json with the proper bot token and owner id)
If config isn't there, it will be auto-generated, but the 
