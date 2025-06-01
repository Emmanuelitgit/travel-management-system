"use client";
import { KeycloakProvider } from "@/contexts/KeycloakContext";

export default function ClientLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return <KeycloakProvider>{children}</KeycloakProvider>;
}
