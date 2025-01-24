package com.game.tama.thing.component;

public class Health extends Component
{
    private int maxHealth;
    private int currentHealth;
    public Health(int maxHealth)
    {
        this.maxHealth = maxHealth;
        currentHealth = maxHealth;
    }

    public Health()
    {
        this(100);
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
