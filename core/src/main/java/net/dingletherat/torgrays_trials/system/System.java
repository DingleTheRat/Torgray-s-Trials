// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.main.World;

public interface System {
    public void draw(World world);
    public void update(World world, float deltaTime);
}
