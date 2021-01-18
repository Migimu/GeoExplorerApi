package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Localizaciones;
import com.example.demo.model.Rutas;
import com.example.demo.repository.LocalizacionRepository;
import com.example.demo.repository.RutaRepository;
import com.example.demo.repository.RutaUsuarioRepository;

@RequestMapping("/rutas")
@RestController
public class RutaController {

	@Autowired
	private RutaRepository rutaRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private LocalizacionRepository localizacionRepository;//necesario para updates y deletes

	@Autowired
	private RutaUsuarioRepository rutaUsuarioRepository;//necesario para updates y deletes ya que están relacionados

	/***********************CREAR***********************/
	//crear ruta
	@PostMapping("/add")
	public void insertarRuta(@RequestBody Rutas nuevaRuta) {
		rutaRepository.save(nuevaRuta);
	}

	/***********************LEER***********************/
	//obtener todas las rutas
	@GetMapping("/getAll")
	public List<Rutas> leerRutas() {
		return rutaRepository.findAll();
	}
	//obtener ruta por Id
	@GetMapping("/getId/{id}")
	public Optional<Rutas> leerRutaId(@PathVariable String id) {
		return rutaRepository.findById(id);
	}
	//obtener todas las rutas de nombre X
	@GetMapping("/getNombre/{nombre}")
	public List<Rutas> leerRutaNombre(@PathVariable String nombre) {
		return rutaRepository.findByNombre(nombre);
	}
	//obtener rutas de una ciudad
	@GetMapping("/getCiudad/{ciudad}")
	public List<Rutas> leerRutaCiudad(@PathVariable String ciudad) {
		return rutaRepository.findRutaByCiudad(ciudad);
	}

	/***********************EDITAR***********************/
	//editar una ruta por su id
	@PutMapping("/editId/{id}")
	public List<Rutas> editarRutaId(@PathVariable String id, @RequestBody Rutas ruta){
		List<Rutas> rutas = rutaRepository.findAllById(id);
		for (Rutas ruta1 : rutas){
			ruta1.setNombre(ruta.getNombre());
			ruta1.setCiudad(ruta.getCiudad());
			ruta1.setTematica(ruta.getTematica());
			ruta1.setDuracion(ruta.getDuracion());
			ruta1.setDescripcion(ruta.getDescripcion());
			ruta1.setTransporte(ruta.getTransporte());
			ruta1.setImagen(ruta.getImagen());
			ruta1.setDificultad(ruta.getDificultad());
			ruta1.setListaLocalizaciones(ruta.getListaLocalizaciones());
		}
		return rutaRepository.saveAll(rutas);
	}
	//editar ruta por su nombre
	@PutMapping("/editNombre/{nombre}")
	public List<Rutas> editarRutaNombre(@PathVariable String nombre, @RequestBody Rutas ruta){
		List<Rutas> rutas = rutaRepository.findByNombre(nombre);
		for (Rutas ruta1 : rutas){
			ruta1.setNombre(ruta.getNombre());
			ruta1.setCiudad(ruta.getCiudad());
			ruta1.setTematica(ruta.getTematica());
			ruta1.setDuracion(ruta.getDuracion());
			ruta1.setDescripcion(ruta.getDescripcion());
			ruta1.setTransporte(ruta.getTransporte());
			ruta1.setImagen(ruta.getImagen());
			ruta1.setDificultad(ruta.getDificultad());
			ruta1.setListaLocalizaciones(ruta.getListaLocalizaciones());
		}
		return rutaRepository.saveAll(rutas);
	}

	//update localizaciones de la ruta
	@PutMapping("/editLocalizacion")
	public Rutas editarRutaLocalizacion(@RequestBody Rutas ruta){
		List<Localizaciones> listaLocalizaciones = localizacionRepository.findByRutaId(ruta.getId());//obtenemos las localizaciones de la ruta
		ruta.setListaLocalizaciones(listaLocalizaciones);//pasamos las localizaciones al array de localizaciones de la ruta
		return rutaRepository.save(ruta);//guardamos los cambios
	}
	/***********************ELIMINAR***********************/
	//eliminamos todas las rutas
	@DeleteMapping("/deleteAll")
	public void eliminarRuta(@RequestBody Rutas ruta) {
		//primero tiene que eliminar las localizaciones
		localizacionRepository.deleteAll();
		//eliminamos rutaUsuarios (ranking)
		rutaUsuarioRepository.deleteAll();
		//eliminamos las rutas
		rutaRepository.deleteAll();
	}
	//eliminar por id
	@DeleteMapping("/deleteId/{id}")
	public void eliminarRutaId(@PathVariable String id) {
		//primero tiene que eliminar las localizaciones
		localizacionRepository.deleteByRutaId(id);
		//eliminamos las rutaUsuario
		rutaUsuarioRepository.deleteByRutaId(id);
		//eliminamos la ruta
		rutaRepository.deleteById(id);
	}
	//eliminar por nombre
	@DeleteMapping("/deleteNombre/{nombre}")
	public void eliminarRutaNombre(@PathVariable String nombre) {

		//cogemos las rutas que deseamos eliminar por nombre
		List<Rutas>lRuta=rutaRepository.findByNombre(nombre);
		//eliminamos las localizaciones recorriendo las rutas 
		for(Rutas r : lRuta){
			localizacionRepository.deleteByRutaId(r.getId());//eliminar localizaciones
			
			rutaUsuarioRepository.deleteByRutaId(r.getId());//eliminamos las rutaUsuario
		}
		//eliminamos las rutas
		rutaRepository.deleteByNombre(nombre);
	}


}