package de.felixperko.worldgen.Generation.Components;

import de.felixperko.worldgen.Generation.Misc.GenerationParameterSupply;

public abstract class NoiseGeneratorMethod {
	public abstract double getValue(GenerationParameterSupply supply);
}
