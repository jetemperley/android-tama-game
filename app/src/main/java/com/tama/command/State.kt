package com.tama.command

interface State
{
    fun update(queue: CommandQueue)
}