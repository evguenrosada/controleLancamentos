import { FormEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login, register, saveSession } from '../services/api';

export function AuthPage({ mode }: { mode: 'login' | 'register' }) {
  const navigate = useNavigate();
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setError('');
    setLoading(true);

    try {
      const auth =
        mode === 'login'
          ? await login(email, senha)
          : await register(nome, email, senha);
      saveSession(auth);
      navigate('/dashboard');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Falha na autenticação');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="container">
      <div className="card" style={{ maxWidth: 420, margin: '40px auto' }}>
        <h1>{mode === 'login' ? 'Entrar' : 'Cadastrar comerciante'}</h1>
        <form onSubmit={handleSubmit}>
          {mode === 'register' ? (
            <div style={{ marginBottom: 12 }}>
              <label htmlFor="nome">Nome</label>
              <input id="nome" value={nome} onChange={(e) => setNome(e.target.value)} required />
            </div>
          ) : null}
          <div style={{ marginBottom: 12 }}>
            <label htmlFor="email">E-mail</label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div style={{ marginBottom: 12 }}>
            <label htmlFor="senha">Senha</label>
            <input
              id="senha"
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
              minLength={6}
            />
          </div>
          {error ? <p className="error">{error}</p> : null}
          <button type="submit" disabled={loading} style={{ width: '100%', marginTop: 8 }}>
            {loading ? 'Aguarde...' : mode === 'login' ? 'Entrar' : 'Cadastrar'}
          </button>
        </form>
        <p style={{ marginTop: 16 }}>
          {mode === 'login' ? (
            <>
              Não tem conta? <Link to="/register">Cadastre-se</Link>
            </>
          ) : (
            <>
              Já tem conta? <Link to="/login">Entrar</Link>
            </>
          )}
        </p>
      </div>
    </div>
  );
}
