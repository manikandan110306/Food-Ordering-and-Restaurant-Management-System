import { useState, useEffect } from 'react'
import api from './services/api'
import './App.css'

function App() {
  const [health, setHealth]=useState(null)
  const [loading,setLoading]=useState(null)
  const [error,setError]=useState('')

  useEffect(()=>{
    api.get('/api/health')
      .then((response)=>{
        setHealth(response.data)
      })
      .catch(()=>{
        setError("Unable to connect to the backend.")
      })
      .finally(()=>{
        setLoading(false)
      })
  },[])

  return (
    <main className="app">

      <header className="navbar">
        <h2 className="brand">Foodie</h2>
        <span className="nac-label">Food Ordering System</span>
      </header>

      <section className="hero">
        <p className='eyebrow'>YOUR FOOD, YOUR WAY</p>

        <h1>
          Welcome to <span>Foodies</span>
        </h1>

        <p className='description'>
          Your Food ordering and restaurant management experience starts here.
        </p>

        <div className="status=card">
          <h2>Backend Connection Status</h2>

          {loading && (
            <p className="status loading">
              Connecting to Spring Boot...
            </p>
          )}

          {!loading && health && (
            <div className="status success">
              <p>Status: {health.status}</p>
              <p>{health.message}</p>
            </div>
          )}

          {!loading && error && (
            <p className="status error">{error}</p>
          )}
        </div>
      </section>

      <footer className="footer">
        Build with React, Java and Spring Boot
      </footer>
    </main>
  )
}
  

export default App
