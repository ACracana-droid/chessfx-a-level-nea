---
title: parletts_move_notation
vaulted-by: Alex Cracana
tags: []
created: 2026-08-10 21:05:24+01:00
updated: 2026-08-11 18:01:31+01:00
---
**This is clipped from https://en.wikipedia.org/wiki/Fairy_chess_piece**

### Parlett's movement notation

In his book _The Oxford History of Board Games_[[19]](https://en.wikipedia.org/wiki/Fairy_chess_piece#cite_note-19) [David Parlett](https://en.wikipedia.org/wiki/David_Parlett "David Parlett") used a notation to describe fairy piece movements. The move is specified in the form m={_expression_}, where _m_ stands for "move", and the _expression_ is composed from the following elements:

- Distance (numbers, n)
    - **1** – a distance of one (i.e. to adjacent square)
    - **2** – a distance of two
    - **n** – any distance in the given direction
- Direction (punctuation, X)
    - **+** – orthogonally (four possible directions)
    - **>** – orthogonally forwards
    - **<** – orthogonally backwards
    - **<>** – orthogonally forwards and backwards
    - **=** – orthogonally sideways (used here instead of Parlett's divide symbol ÷)
    - **>=** – orthogonally forwards or sideways
    - **<=** – orthogonally backwards or sideways
    - **X** – diagonally (four possible directions)
    - **X>** – diagonally forwards
    - **X<** – diagonally backwards
    - **✳** – orthogonally or diagonally (all eight possible directions); same as +X; Parlett uses *
- Grouping
    - **/** – two orthogonal moves separated by a [slash](https://en.wikipedia.org/wiki/Slash_\(punctuation\) "Slash (punctuation)") denote a hippogonal move (i.e. jumps like a knight)
    - **&** – repeated movement in the same direction, such as for hippogonal riders (e.g., the nightrider)
    - **.** – then, (e.g., an aanca is 1X.n+; one step diagonally and _then_ any distance orthogonally outwards)

#### Additions to Parlett's

The following can be added to Parlett's to make it more complete:[_[citation needed](https://en.wikipedia.org/wiki/Wikipedia:Citation_needed "Wikipedia:Citation needed")_]

- Conditions under which the move may occur (lowercase alphanumeric, except n)
    - (default) – May occur at any point in the game
    - **i** – May only be made on the initial move (e.g. pawn's 2 moves forward)
    - **c** – May only be made on a capture (e.g. pawn's diagonal capture)
    - **o** – May not be used for a capture (e.g. pawn's forward move)
- Move type
    - (default) – Captures by landing on the piece; blocked by intermediate pieces
    - **~** – Leaper (leaps); captures by landing on the opposing piece
    - **^** – Locust (captures by hopping; implies hopper); capture move is one square past the captured piece
- Grouping (punctuation)
    - **,** (comma) – separates move options; only one of the comma-delimited options may be chosen per move
    - **()** – grouping operator; see nightrider
    - **-** – range operator

The format (not including grouping) is: <conditions> <move type> <distance> <direction> <other>

On this basis, the traditional chess moves (excluding castling and _en passant_ capture) are:

- King: 1✳
- Queen: n✳
- Bishop: nX
- Rook: n+
- Pawn: o1>, c1X>, oi2>
- Knight: ~1/2