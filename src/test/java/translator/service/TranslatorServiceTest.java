package translator.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import translator.Application;
import translator.domain.TranslatedText;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TranslatorServiceTest {

  @Autowired
  TranslatorService translatorService;

  @Test
  public void translateTest_en_es() {
    TranslatedText translatedText = translatorService.translate("en", "es", "This is a test of translation service");
    assertEquals("Esto es una prueba de servicio de traducción", translatedText.getTranslation());
  }
  
  @Test
  public void translateTest_es_pl() {
    TranslatedText translatedText = translatorService.translate("es", "pl", "Buenos dias a todos");
    assertEquals("Dzień dobry wszystkim", translatedText.getTranslation());
  }

  @Test
  public void translateTest_en_ru() {
    TranslatedText translatedText = translatorService.translate("en", "ru", "Thank you for using our service");
    assertEquals("Благодарим Вас за использование нашего сервиса", translatedText.getTranslation());
  }

}
