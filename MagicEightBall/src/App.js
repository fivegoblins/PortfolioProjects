import React, {Component} from 'react';
import './App.css';


class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      input: '',
      random: '',
      active: false
    }

    this.ask = this.ask.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.toggleClass = this.toggleClass.bind(this);

  }


  ask() {
    if (this.state.input) {
      this.setState({
        random: Math.floor(Math.random() * 21),
        input: '',
        active: false
      });
    }
  }

  toggleClass() {
    const currentState = this.state.active;
    this.setState({active: !currentState});
  }

  handleChange(event) {
    event.preventDefault();
    this.setState({input: event.target.value});
  }

  render() {
    const responses = [
      'It is certain.',
      'It is decidedly so.',
      'Without a doubt!', 
      'Yes, definitely!',
      'You may rely on it.',
      'As I see it, yes.',
      'Outlook good!',
      'Yes.',
      'Signs point to yes.',
      'Reply hazy, try again.',
      'Ask again later.',
      'Better not tell you now.',
      'Cannot predict now.',
      'Concentrate and ask again.',
      'Don\'t count on it.', 
      'No.',
      'My sources say no',
      'Most likely',
      'Outlook not so good.',
      'Very doubtful.'
    ];


    //var answer = responses[this.state.random - 1];

    return (
      <div className='App'>
        <div className='qa-div'>
          <header className='header'>
            Magic 8 Ball
          </header>
          <div className='question-div'>
            <input 
              className='question-input'
              type='text'
              placeholder='Enter your question'
              value={this.state.input}
              onChange={this.handleChange}
              onClick={this.toggleClass}
            />
            <button 
              className='ask-button'
              onClick={this.ask}
            >
              Ask
            </button>
            <h3 className={this.state.active ? 'hidden' : 'answer'}>
            {this.answer = responses[this.state.random - 1]}
          </h3>
          </div>
        </div>
        <div className='ball'>
          <span className='shadow'></span>
          <span className='eight'></span>
        </div>
        </div>
    );
  }
}

export default App;
