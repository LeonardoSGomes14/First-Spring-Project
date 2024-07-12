package br.com.alura.screenmatch.service.translate;

import br.com.alura.screenmatch.service.ApiConsumption;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class MyMemoryQuery {
    public static String GetTranslate(String text) {
        ObjectMapper mapper = new ObjectMapper();

        ApiConsumption consumo = new ApiConsumption();

        String texto = URLEncoder.encode(text);
        String langpair = URLEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q=" + texto + "&langpair=" + langpair;

        String json = consumo.getData(url);

        TranslateData traducao;
        try {
            traducao = mapper.readValue(json, TranslateData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return traducao.responseData().textoTraduzido();
    }
}

