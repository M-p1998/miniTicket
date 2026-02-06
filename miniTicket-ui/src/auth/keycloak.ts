import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8088",
  realm: "miniTicket",
  clientId: "miniTicket-ui",
});

export default keycloak;
