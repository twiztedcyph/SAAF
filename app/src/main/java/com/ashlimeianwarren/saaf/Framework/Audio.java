package com.ashlimeianwarren.saaf.Framework;

public interface Audio
{
    public com.ashlimeianwarren.saaf.Implementation.AndroidMusic createMusic(String filename);

    public com.ashlimeianwarren.saaf.Implementation.AndroidSound createSound(String filename);
}
