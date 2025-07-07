import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8080",
  realm: "travelManagement",
  clientId: "RGa0LicOm5XynGZcTOFjh5NElYjhsNGh",
});

export default keycloak;
