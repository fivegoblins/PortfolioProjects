import React, { useState } from 'react';
import './App.css';

import TodoForm from './TodoForm';
import Todo from './Todo';

function App() {
  const [todos, setTodos] = useState([
    {text: "Laundry",
      isCompleted: false},
    {text: "Walk the dog",
      isCompleted: false},
    {text: "Take out the trash", 
    isCompleted: false}
  ]);

  const addTodo = text=> {
    const newTodos = [...todos, { text }];
    setTodos(newTodos);
  }

  const completeTodo = index=> {
    const newTodos = [...todos];
    newTodos[index].isCompleted = true;
    setTodos(newTodos);
  }

  const removeTodo = index=> {
    const newTodos = [...todos];
    newTodos.splice(index, 1);
    setTodos(newTodos);
  }

  return (
    <div className='app'>
      <div className='todo-list'>
        {todos.map((todo, i)=> (
          <Todo 
            key={i}
            index={i}
            todo={todo}
            completeTodo={completeTodo}
            removeTodo={removeTodo}
          />
        ))}
        <TodoForm 
          addTodo={addTodo}
        />
      </div>
    </div>
  );
}

export default App;
