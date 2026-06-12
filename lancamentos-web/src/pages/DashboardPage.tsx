import { FormEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  clearSession,
  createLancamento,
  getComercianteNome,
  isAuthenticated,
  listLancamentos,
  listSaldos,
  type Lancamento,
  type SaldoDiario,
} from '../services/api';

export function DashboardPage() {
  const navigate = useNavigate();
  const [lancamentos, setLancamentos] = useState<Lancamento[]>([]);
  const [saldos, setSaldos] = useState<SaldoDiario[]>([]);
  const [tipo, setTipo] = useState<'CREDITO' | 'DEBITO'>('CREDITO');
  const [valor, setValor] = useState('');
  const [dataReferencia, setDataReferencia] = useState(new Date().toISOString().slice(0, 10));
  const [descricao, setDescricao] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate('/login');
      return;
    }
    void loadData();
  }, [navigate]);

  async function loadData() {
    try {
      const [lancamentosData, saldosData] = await Promise.all([listLancamentos(), listSaldos()]);
      setLancamentos(lancamentosData);
      setSaldos(saldosData);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao carregar dados');
    }
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setError('');
    setLoading(true);
    try {
      await createLancamento({
        tipo,
        valor: Number(valor),
        dataReferencia,
        descricao: descricao || undefined,
      });
      setValor('');
      setDescricao('');
      await loadData();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao registrar lançamento');
    } finally {
      setLoading(false);
    }
  }

  function logout() {
    clearSession();
    navigate('/login');
  }

  return (
    <div className="container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Olá, {getComercianteNome()}</h1>
        <button className="secondary" type="button" onClick={logout}>
          Sair
        </button>
      </div>

      <div className="card">
        <h2>Novo lançamento</h2>
        <form onSubmit={handleSubmit}>
          <div className="grid-2">
            <div>
              <label htmlFor="tipo">Tipo</label>
              <select id="tipo" value={tipo} onChange={(e) => setTipo(e.target.value as 'CREDITO' | 'DEBITO')}>
                <option value="CREDITO">Crédito</option>
                <option value="DEBITO">Débito</option>
              </select>
            </div>
            <div>
              <label htmlFor="valor">Valor</label>
              <input
                id="valor"
                type="number"
                min="0.01"
                step="0.01"
                value={valor}
                onChange={(e) => setValor(e.target.value)}
                required
              />
            </div>
            <div>
              <label htmlFor="data">Data</label>
              <input
                id="data"
                type="date"
                value={dataReferencia}
                onChange={(e) => setDataReferencia(e.target.value)}
                required
              />
            </div>
            <div>
              <label htmlFor="descricao">Descrição</label>
              <input id="descricao" value={descricao} onChange={(e) => setDescricao(e.target.value)} />
            </div>
          </div>
          {error ? <p className="error">{error}</p> : null}
          <button type="submit" disabled={loading} style={{ marginTop: 12 }}>
            {loading ? 'Salvando...' : 'Registrar'}
          </button>
        </form>
      </div>

      <div className="card">
        <h2>Lançamentos</h2>
        <table>
          <thead>
            <tr>
              <th>Data</th>
              <th>Tipo</th>
              <th>Valor</th>
              <th>Descrição</th>
            </tr>
          </thead>
          <tbody>
            {lancamentos.map((item) => (
              <tr key={item.id}>
                <td>{item.dataReferencia}</td>
                <td>{item.tipo}</td>
                <td>{item.valor.toFixed(2)}</td>
                <td>{item.descricao ?? '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="card">
        <h2>Saldo diário consolidado</h2>
        <table>
          <thead>
            <tr>
              <th>Data</th>
              <th>Créditos</th>
              <th>Débitos</th>
              <th>Saldo</th>
            </tr>
          </thead>
          <tbody>
            {saldos.map((item) => (
              <tr key={item.id}>
                <td>{item.dataReferencia}</td>
                <td>{item.totalCreditos.toFixed(2)}</td>
                <td>{item.totalDebitos.toFixed(2)}</td>
                <td>{item.saldo.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
