import React, { useEffect, useState } from 'react'
import { obtenerEstado, avanzar, reiniciar } from './api/CarreraAPI.js'
import Tortuga from './componentes/Tortuga.jsx'

function App() {
  const [tortugas, setTortugas] = useState({})

  const cargarEstado = async () => {
    const data = await obtenerEstado()
    setTortugas(data)
  }

  useEffect(() => {
    cargarEstado()
    const intervalo = setInterval(cargarEstado, 2000) // actualiza cada 2 seg
    return () => clearInterval(intervalo)
  }, [])

  const handleAvanzar = async (id) => {
    await avanzar(id)
    cargarEstado()
  }

  const handleReiniciar = async () => {
    await reiniciar()
    cargarEstado()
  }

  return (
    <div style={{ padding: '30px', maxWidth: '600px', margin: 'auto' }}>
      <h1>Carrera de Tortugas 🐢</h1> 
      {Object.keys(tortugas).length === 0 ? (
        <p>Cargando tortugas...</p>
      ) : (
        Object.entries(tortugas).map(([id, pos]) => (
          <Tortuga key={id} id={id} posicion={pos} onAvanzar={() => handleAvanzar(id)} />
        ))
      )}
      <hr />
      <button onClick={handleReiniciar}>Reiniciar Carrera</button>
    </div>
  )
}

export default App