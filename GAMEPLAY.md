# City of Kalux - Gameplay Guide

## Overview

*City of Kalux* is a keyboard-controlled, story-driven 2D investigation game. You play as Andrew King, a paper-factory worker in the domed city of Kalux. After his friend and colleague Lilith Joy Wings is reported to have died in an accident, Andrew investigates the circumstances of her death and the city's political factions.

The story is presented through an opening sequence, conversations, staged story panels, and entries in Andrew's notebook. The game begins at Andrew's home, 10 Chopper Way.

## How to play

Explore the locations, find evidence, and speak with people who may know about Lilith. Many story steps depend on reaching a particular room, collecting an item, using it in the right place, or finishing a conversation. Move close to a character and face them before starting a conversation.

The world uses a tile grid. Walls, furniture, closed doors, and characters can block movement. A fog-of-war effect hides unexplored areas and clears as Andrew explores.

### Keyboard controls

| Key | Action |
| --- | --- |
| Arrow keys | Move Andrew |
| Space | Start talking to an adjacent character in the direction Andrew is facing |
| Q / W | Choose the first / second dialogue response |
| C | Pick up a nearby item; the backpack is required before other items can be carried |
| D | Drop the first item in the backpack |
| 1-9 | Use the item in the corresponding backpack slot |
| M | Show the travel map; press M and 1-5 together to travel to a location |
| N | Open or close the notebook |
| 1-5 while the notebook is open | Select the introduction, tutorial, or one of the story sections |
| Left / Right arrows while the notebook is open | Move between notebook entries |
| Z | Show or hide achievements |
| Enter | Advance the opening story sequence and staged story panels |
| A | Call the sidekick to follow Andrew, when a sidekick is present |
| S | Attempt to steal money from an adjacent character Andrew is facing |

Some keys have a context-dependent meaning: number keys select map destinations while the travel map is open, and select notebook sections while the notebook is open. Otherwise, they use the corresponding backpack slot.

### Travel, items, and money

The travel map has five destinations:

1. Andrew's home (10 Chopper Way)
2. Paper Factory
3. Hayden Park
4. Officials' HQ
5. Wilderness

Travel to a different location costs $5. Andrew starts with $30, and the game adds $10 periodically while gameplay is running. The backpack holds up to nine items. Some objects provide information; keys and other items can have location-specific uses. The inventory display shows carried items and any message or image revealed by using them.

Stealing is optional: an attempt is made against the character Andrew is facing, and the result and amount are randomized. Talking to a character can improve the relationship over repeated conversations. Character highlights indicate friendly or hostile status.

## Story progression

The following outline contains plot spoilers.

1. **Introduction and tutorial:** Andrew leaves home to investigate Lilith's death at the Paper Factory. The early objectives introduce collecting and dropping items, travelling, exploring under fog of war, talking, and using inventory items.
2. **Investigation:** Andrew questions people connected to Lilith, including Officials and members of a rebel group. Evidence found at the factory changes his understanding of Lilith's role and points him toward the Wilderness.
3. **Capture sequence (conditional):** Talking to a hostile character during the investigation can trigger a pursuit by Officials. If the capture sequence is triggered, Andrew is taken to a cell at Officials' HQ; finding and using the required key lets him escape and resume the interrupted story.
4. **Wilderness and the Leader:** Andrew speaks with Charlie and then the Leader. The resulting conversations identify the killer and reveal the conflict between the Officials and the rebellion.

Story notes appear in the notebook as the investigation progresses. The game defines achievements for reaching at least $100 and reaching the capture event; other achievement slots are currently unused.

## Current implementation notes

- The guide reflects the behavior and story text currently present in the game code and bundled notes.
- The game does not currently present a clear, explicit victory or ending screen in the reviewed flow; the guide therefore describes the implemented story events without claiming a completed ending.
- Some story beats and dialogue rely on specific characters, rooms, collected items, and event stages. If a sequence appears stalled, revisit the notebook, talk to nearby characters, and check whether an item needs to be used.
