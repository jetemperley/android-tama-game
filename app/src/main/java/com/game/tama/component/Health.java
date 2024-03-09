package com.game.tama.component;

import com.game.tama.thing.Thing;

public class Health extends Component
{
    private int maxHealth;
    private int currentHealth;
    public Health(Thing parent, int maxHealth)
    {
        super(parent);
        this.maxHealth = maxHealth;
        currentHealth = maxHealth;
    }

    public Health(Thing parent)
    {
        this(parent, 100);
    }

    public void addHealth(int dam)
    {
        currentHealth += dam;
    }

    public void setHealth(int health)
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
