import React from 'react';
import './App.css';

const Todo = ({todo, index, completeTodo, removeTodo}) => 
    <div 
        className='todo'
        style={{ textDecoration: todo.isCompleted ? "line-through" : ""}}
    >
        {todo.text}
    <div className='button-div'>
        <button 
            onClick={()=> completeTodo(index)}
            className='complete'
        >
        </button>
        <button 
            onClick={()=> removeTodo(index)}
            className='remove'
        >
        </button>
    </div>
    </div>

export default Todo;