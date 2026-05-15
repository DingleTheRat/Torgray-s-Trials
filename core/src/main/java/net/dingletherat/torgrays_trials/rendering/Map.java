// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import java.util.HashMap;

import net.dingletherat.torgrays_trials.rendering.TileManager.Position;

public record Map (HashMap<Position, Integer> ground, HashMap<Position, Integer> foreground,
                   int x, int y) {}
