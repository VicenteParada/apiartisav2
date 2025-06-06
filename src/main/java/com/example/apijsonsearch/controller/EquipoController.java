package com.example.apijsonsearch.controller;

import com.example.apijsonsearch.model.Equipo;
import com.example.apijsonsearch.service.SitrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EquipoController {

    @Autowired
    private SitrackService sitrackService;

    @GetMapping("/")
    public String mostrarFormularioBusqueda() {
        return "index";  // Tu formulario de búsqueda está en index.html
    }

    @GetMapping("/buscar")
    public String buscarEquipo(@RequestParam("texto") String texto, Model model) {
        Equipo equipo = sitrackService.buscarEquipoPorTexto(texto);

        if (equipo != null) {
            model.addAttribute("equipo", equipo);
            return "detalle";  // Página con detalle del equipo
        } else {
            model.addAttribute("noEncontrado", true);
            model.addAttribute("textoBuscado", texto);
            return "index";  // Devuelve a index con mensaje de no encontrado
        }
    }
}

