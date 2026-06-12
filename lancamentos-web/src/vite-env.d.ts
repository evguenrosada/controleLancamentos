/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_LANCAMENTOS_API_URL?: string;
  readonly VITE_SALDOS_API_URL?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
