const API_URL = 'http://localhost:8080/tortugas'

export async function obtenerEstado() {
  const response = await fetch(`${API_URL}/estado`)
  return response.json()
}

export async function avanzar(id) {
  const response = await fetch(`${API_URL}/${id}/avanzar`, {
    method: 'POST'
  })
  return response.json()
}

export async function reiniciar() {
  const response = await fetch(`${API_URL}/reiniciar`, {
    method: 'POST'
  })
  return response.text()
}