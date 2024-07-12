package br.com.alura.screenmatch.service.translate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseData( @JsonAlias(value = "translatedText") String textoTraduzido) {
}
