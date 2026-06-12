const LANCAMENTOS_API = import.meta.env.VITE_LANCAMENTOS_API_URL ?? 'http://localhost:8081';
const SALDOS_API = import.meta.env.VITE_SALDOS_API_URL ?? 'http://localhost:8082';

export type AuthResponse = {
  comercianteId: string;
  nome: string;
  email: string;
  accessToken: string;
};

export type Lancamento = {
  id: string;
  comercianteId: string;
  tipo: 'DEBITO' | 'CREDITO';
  valor: number;
  dataReferencia: string;
  descricao?: string;
  createdAt: string;
};

export type SaldoDiario = {
  id: string;
  comercianteId: string;
  dataReferencia: string;
  totalCreditos: number;
  totalDebitos: number;
  saldo: number;
  updatedAt: string;
};

function getToken(): string | null {
  return localStorage.getItem('accessToken');
}

async function request<T>(url: string, init: RequestInit = {}): Promise<T> {
  const headers = new Headers(init.headers);
  headers.set('Content-Type', 'application/json');
  const token = getToken();
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  const response = await fetch(url, { ...init, headers });
  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    throw new Error(body.message ?? `Erro HTTP ${response.status}`);
  }
  return response.json() as Promise<T>;
}

export async function register(nome: string, email: string, senha: string) {
  return request<AuthResponse>(`${LANCAMENTOS_API}/api/auth/register`, {
    method: 'POST',
    body: JSON.stringify({ nome, email, senha }),
  });
}

export async function login(email: string, senha: string) {
  return request<AuthResponse>(`${LANCAMENTOS_API}/api/auth/login`, {
    method: 'POST',
    body: JSON.stringify({ email, senha }),
  });
}

export async function listLancamentos() {
  return request<Lancamento[]>(`${LANCAMENTOS_API}/api/lancamentos`);
}

export async function createLancamento(payload: {
  tipo: 'DEBITO' | 'CREDITO';
  valor: number;
  dataReferencia: string;
  descricao?: string;
}) {
  return request<Lancamento>(`${LANCAMENTOS_API}/api/lancamentos`, {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export async function listSaldos() {
  return request<SaldoDiario[]>(`${SALDOS_API}/api/saldos`);
}

export function saveSession(auth: AuthResponse) {
  localStorage.setItem('accessToken', auth.accessToken);
  localStorage.setItem('comercianteNome', auth.nome);
}

export function clearSession() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('comercianteNome');
}

export function isAuthenticated() {
  return Boolean(getToken());
}

export function getComercianteNome() {
  return localStorage.getItem('comercianteNome') ?? 'Comerciante';
}
