"use client";

import { createContext, useContext, useEffect, useState } from "react";
import keycloak from "../lib/keycloak";

const KeycloakContext = createContext<any>(null);

export const KeycloakProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (typeof window !== "undefined") {
      keycloak
        .init({
          onLoad: "login-required",
          checkLoginIframe: false,
          pkceMethod: "S256",
        })
        .then((auth) => {
          setAuthenticated(auth);
          setLoading(false);
        })
        .catch((err) => {
          console.error("Keycloak init failed", err);
          setLoading(false);
        });
    }
  }, []);

  if (loading) return <div>Loading...</div>;

  if (!authenticated) return <div>Redirecting to login...</div>;

  return (
    <KeycloakContext.Provider value={{ keycloak, authenticated }}>
      {children}
    </KeycloakContext.Provider>
  );
};

export const useKeycloak = () => useContext(KeycloakContext);
