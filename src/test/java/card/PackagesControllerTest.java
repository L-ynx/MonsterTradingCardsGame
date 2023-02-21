package card;

import org.fhtw.application.controller.packages.PackagesController;
import org.fhtw.application.model.Card;
import org.fhtw.application.repository.PackageRepository;
import org.fhtw.http.Request;
import org.fhtw.http.Response;
import org.fhtw.http.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PackagesControllerTest {
    static PackagesController packagesController;
    static Request req;
    static Method createPackage;

    @BeforeAll
    static void initPackagesController() throws Exception {
        PackageRepository packageRepository = mock(PackageRepository.class);
        packagesController = new PackagesController(packageRepository);
        req = new Request();
        req.setBody("[{\"Id\":\"845f1dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"99f8f8dc-e25e-4a15-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"e85e3976-7c86-4d06-9a80-651c2019a79f\", \"Name\":\"WaterSpell\", \"Damage\": 20.0}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab589334\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f1b3439f\", \"Name\":\"FireSpell\", \"Damage\": 25.0}]");

        List<Card> cards = req.getBodyAsList(Card.class);

        when(packageRepository.authenticate("admin", "admin-mtcgToken")).thenReturn(true);
        when(packageRepository.authenticate("Midoriya", "Midoriya-mtcgToken")).thenReturn(true);
        when(packageRepository.createPackage(refEq(cards))).thenReturn(true);

        createPackage = packagesController.getClass().getDeclaredMethod("createPackage", Request.class);
        createPackage.setAccessible(true);
    }


    // Only admin can create packages
    @Test
    void validCreatePackage() throws Exception {
        req.setUsername("admin");
        req.setToken("admin-mtcgToken");

        Object response = createPackage.invoke(packagesController, req);

        assertEquals(Status.CREATED, ((Response) response).getHttpStatus());
    }

    @Test
    void notAdmin() throws Exception {
        req.setUsername("Midoriya");
        req.setToken("Midoriya-mtcgToken");

        Object response = createPackage.invoke(packagesController, req);

        assertEquals(Status.FORBIDDEN, ((Response) response).getHttpStatus());
        assertEquals("Provided user is not 'admin'", ((Response) response).getBody());
    }

    @Test
    void tokenMissing() throws Exception {
        req.setUsername("admin");

        Object response = createPackage.invoke(packagesController, req);

        assertEquals(Status.UNAUTHORIZED, ((Response) response).getHttpStatus());
        assertEquals("Access token is missing or invalid", ((Response) response).getBody());
    }
}
