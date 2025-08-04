package com.game.tama.core;

import java.util.Random;

public class Noise
{

    private final Random r1;
    private final Random r2;
    private final Random r3;
    private final Random rs;
    private final int seed;
    private final float norm;
    private final int size;

    // sigma is the normalization constant 0 for raw, 1 for smooth
    // size is the width of the average square edge
    public Noise(final int seedIn, final int sizeIn, final float sigma)
    {
        norm = sigma;
        size = sizeIn;
        r1 = new Random();
        r2 = new Random();
        r3 = new Random();
        seed = seedIn;
        rs = new Random(seed);
    }

    public Noise(final int seedIn)
    {
        this(seedIn, 3, 0.175f);
    }

    // produces a psuedorandom value specific to x, y, and seed
    // but independent from x or y alone
    public float getCoord(int x, int y)
    {

        rs.setSeed(seed);
        r1.setSeed(x + rs.nextInt());
        x = r1.nextInt();
        r2.setSeed(y + rs.nextInt());
        y = r2.nextInt();

        // reconsider this line
        final int z = ((x * y) % rs.nextInt());
        // System.out.print(z + " ");

        r3.setSeed(z);
        return r3.nextFloat();
    }

    // uses the average of the 9 rand coords around x, y
    // and re maps from a normal distribution to a
    // *roughly* normal distribution 0-1
    public float getNoise(final int x, final int y)
    {
        float f = 0;
        final int lim = size / 2;
        for (int i = -lim; i <= lim; i++)
        {
            for (int j = -lim; j <= lim; j++)
            {
                f += getCoord(x + i, y + j);
            }
        }
        f /= (2 * lim + 1) * (2 * lim + 1);
        return (float) (cdf(f, 0.5, norm));
    }

    // the below methods are taken from
    // https://introcs.cs.princeton.edu/java/22library/Gaussian.java.html
    // and are utilities for the noise generation,
    // cdf = cumulative density fuciton
    // pdf = probability density function

    // return cdf(z) = standard Gaussian cdf using Taylor approximation
    public static double cdf(final double z)
    {
        if (z < -8.0)
        {
            return 0.0;
        }
        if (z > 8.0)
        {
            return 1.0;
        }
        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2)
        {
            sum = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * pdf(z);
    }

    // return cdf(z, mu, sigma) = Gaussian cdf with mean mu and stddev sigma
    public static double cdf(final double z, final double mu, final double sigma)
    {
        return cdf((z - mu) / sigma);
    }

    public static double pdf(final double x)
    {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }

    // return pdf(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
    public static double pdf(final double x, final double mu, final double sigma)
    {
        return pdf((x - mu) / sigma) / sigma;
    }

}
