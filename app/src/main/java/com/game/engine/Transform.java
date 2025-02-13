package com.game.engine;

public interface Transform<T extends Transform>
{
    public void preMult(T... t);

    public void postMult(T... t);

    public void preTranslate(float x, float y, float z);

    public void postTranslate(float x, float y, float z);

    public void preScale(float x, float y, float z);

    public void postScale(float x, float y, float z);

    public void setValues(T t);
}
