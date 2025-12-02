import React from 'react'

export default function Tortuga({ nombre, posicion, onAvanzar }) {
  return (
    <div className="p-4 border rounded-x1 bg-white shadow-md text-center">
      <h2 className="text-lg font-bold text green-700">{nombre}</h2>
      <div claassName="h-4 bg-gray-200 rounded-full mt-2 mb-3 overflow-hidden">
        <div className="bg-green-500 h-4" style={{ width: `${posicion}%`, transition: "width 0.5s"}}></div>
      </div>
      <p className="text-gray-700 mb-2"> Posicion:{posicion}</p>
      {onAvanzar && (<button onClick={onAvanzar} className="bg-green-600 text-white px-4 py-1 rounded hover:bg-green-700"> Avanzar </button>)}
    </div>
  );
}