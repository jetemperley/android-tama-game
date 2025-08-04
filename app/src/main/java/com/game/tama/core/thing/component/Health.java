package com.game.tama.core.thing.component;

public class Health extends Component
{
    private final int maxHealth;
    private int currentHealth;

    public Health(final int maxHealth)
    {
        this.maxHealth = maxHealth;
        currentHealth = maxHealth;
    }

    public Health()
    {
        this(100);
    }

    public void addHealth(final int dam)
    {
        currentHealth += dam;
    }

    public void setHealth(final int health)
    {
        currentHealth = health;
        if (currentHealth > maxHealth)
        {
            currentHealth = maxHealth;
        }
        else if (currentHealth < 0)
        {
            currentHealth = 0;
        }
    }

    public int getHealth()
    {
        return currentHealth;
    }
}
