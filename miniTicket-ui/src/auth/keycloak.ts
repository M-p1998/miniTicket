import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://4.153.75.53:8080",
  realm: "miniTicket",
  clientId: "miniTicket-ui",
});

export default keycloak;
