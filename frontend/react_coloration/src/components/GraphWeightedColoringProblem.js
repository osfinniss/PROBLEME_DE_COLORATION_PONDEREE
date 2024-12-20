import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import GraphView from './GraphView';
import GraphColoringTable from './GraphColoringTable';


const colorMapping = {
    0: '#FF0000',
    1: '#00FF00',
    2: '#0000FF',
    3: '#FFFF00',
    4: '#FF00FF',
    5: '#00FFFF',
    6: '#800080',
    7: '#FFA500',
    8: '#A52A2A',
    9: '#8B4513',
    10: '#2E8B57',
    11: '#708090',
    12: '#6A5ACD',
    13: '#4682B4',
    14: '#D2691E',
    15: '#DC143C',
    16: '#000080',
    17: '#ADFF2F',
    18: '#FF1493',
    19: '#FFD700',
    20: '#556B2F',
    21: '#B22222',
    22: '#FF69B4',
    23: '#CD5C5C',
    24: '#8A2BE2',
    25: '#5F9EA0',

};

function GraphWeightedColoringProblem() {
    const [graphData, setGraphData] = useState({ nodes: [], edges: [] });
    const [graphList, setGraphList] = useState([]);
    const [selectedGraph, setSelectedGraph] = useState('');
    const [selectedAlgo, setSelectedAlgo] = useState('glouton');
    const [selectedMethod, setSelectedMethod] = useState('hillclimbing');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [cout, setCout] = useState(null);
    const [executionTime, setExecutionTime] = useState(null);

    const baseUrl = 'http://localhost:8080/api';
    const navigate = useNavigate();

    useEffect(() => {
        const fetchGraphList = async () => {
            try {
                const response = await fetch(`${baseUrl}/listeGraphe`);
                if (!response.ok) {
                    throw new Error('Erreur lors de la récupération de la liste des graphes.');
                }
                const data = await response.json();
                setGraphList(data);
                setSelectedGraph(data[0] || '');
            } catch (err) {
                setError('Erreur : ' + err.message);
            }
        };

        fetchGraphList();
    }, []);

    const fetchWeightedGraphData = async (graph, algo, method) => {
        setLoading(true);
        setError('');
        setExecutionTime(null);

        try {
            const startTime = performance.now();

            const response = await fetch(`${baseUrl}/colorationPonderee/${graph}/${algo}/${method}`);
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des données.');
            }

            const data = await response.json();

            const endTime = performance.now();
            setExecutionTime((endTime - startTime).toFixed(2));


            setGraphData({
                nodes: data.nodes.map(node => ({
                    ...node,
                    label: node.id.toString() + "(" + node.poids.toString() + ")",
                    color: colorMapping[node.couleur] || '#000000',
                })),
                edges: data.edges || [],
            });

            setCout(data.cout);
        } catch (err) {
            setError('Erreur : ' + err.message);
        }

        setLoading(false);
    };

    const handleAlgoChange = (event) => {
        setSelectedAlgo(event.target.value);
    };

    const handleMethodChange = (event) => {
        setSelectedMethod(event.target.value);
    };

    const handleLoadGraph = () => {
        fetchWeightedGraphData(selectedGraph, selectedAlgo, selectedMethod);
    };

    return (
        <div>
            <h2>Problème de coloration de graphe pondérée</h2>

            <div style={{ marginBottom: '20px' }}>
                <label htmlFor="graph-select" style={{ marginRight: '10px' }}>Sélectionnez un graphe:</label>
                <select
                    id="graph-select"
                    value={selectedGraph}
                    onChange={(e) => setSelectedGraph(e.target.value)}
                    style={{ padding: '10px', marginRight: '10px' }}
                >
                    {graphList.map((graph, index) => (
                        <option key={index} value={graph}>{graph}</option>
                    ))}
                </select>

                <label htmlFor="algo-select" style={{ marginRight: '10px' }}>Choisissez l'algorithme:</label>
                <select
                    id="algo-select"
                    value={selectedAlgo}
                    onChange={handleAlgoChange}
                    style={{ padding: '10px', marginRight: '10px' }}
                >
                    <option value="glouton">Glouton</option>
                    <option value="dsatur">DSATUR</option>
                    <option value="basique">Basique</option>
                    <option value="random">Random</option>
                </select>

                <label htmlFor="method-select" style={{ marginRight: '10px' }}>Choisissez la méthode:</label>
                <select
                    id="method-select"
                    value={selectedMethod}
                    onChange={handleMethodChange}
                    style={{ padding: '10px', marginRight: '10px' }}
                >
                    <option value="hillclimbing">Hill Climbing</option>
                    <option value="compactage">Compactage</option>
                    <option value="compactage2">Compactage 2</option>
                </select>

                <button onClick={handleLoadGraph} style={{ padding: '10px' }}>
                    Charger la Coloration Pondérée du Graphe
                </button>
            </div>

            {loading && <p>Chargement des données...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {!loading && graphData.nodes.length > 0 && (
                <div style={{ marginBottom: '20px', textAlign: 'center', fontSize: '24px', color: '#FF5733' }}>
                    <strong>Coût total: {cout}</strong>
                </div>
            )}
            {!loading && executionTime && (
                <div style={{ marginBottom: '20px', textAlign: 'center', fontSize: '18px', color: '#28A745' }}>
                    <strong>Temps d'exécution: {executionTime} ms</strong>
                </div>
            )}
            {!loading && graphData.nodes.length > 0 && (
                <>
                    <GraphColoringTable graphData={graphData} />
                    <GraphView nodes={graphData.nodes} edges={graphData.edges} />
                </>
            )}
        </div>
    );
}

export default GraphWeightedColoringProblem;
