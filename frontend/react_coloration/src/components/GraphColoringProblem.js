import React, { useState, useEffect } from 'react';
import GraphView from './GraphView';


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

function GraphColoringProblem() {
    const [graphData, setGraphData] = useState({ nodes: [], edges: [] });
    const [graphList, setGraphList] = useState([]);
    const [selectedGraph, setSelectedGraph] = useState('');
    const [selectedAlgo, setSelectedAlgo] = useState('glouton');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [cout, setCout] = useState(null);
    const [numColorsUsed, setNumColorsUsed] = useState(0);
    const [showBestScoresGCP, setShowBestScoresGCP] = useState(false);
    const [showBestScoresWVCP, setShowBestScoresWVCP] = useState(false);
    const [bestScoresGCP, setBestScoresGCP] = useState([]);
    const [bestScoresWVCP, setBestScoresWVCP] = useState([]);
    const [executionTime, setExecutionTime] = useState(0);

    const baseUrl = 'http://localhost:8080/api';

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

    const fetchGraphColoringData = async (graph, algo) => {
        setLoading(true);
        setError('');
        const startTime = performance.now();
        try {
            const response = await fetch(`${baseUrl}/coloration/${graph}/${algo}`);
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des données.');
            }
            const data = await response.json();

            const coloredNodes = data.nodes.map(node => ({
                ...node,
                label: node.id.toString() + "(" + node.poids.toString() + ")",
                color: colorMapping[node.couleur] || '#000000'
            }));

            setGraphData({
                nodes: coloredNodes,
                edges: data.edges || [],
            });

            setCout(data.cout);

            const uniqueColors = new Set(coloredNodes.map(node => node.couleur));
            setNumColorsUsed(uniqueColors.size);

            const endTime = performance.now();
            setExecutionTime((endTime - startTime).toFixed(2));
        } catch (err) {
            setError('Erreur : ' + err.message);
        }
        setLoading(false);
    };

    const fetchBestScoresGCP = async () => {
        try {
            const response = await fetch(`${baseUrl}/best_scores_gcp`);
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des meilleurs scores GCP.');
            }
            const data = await response.json();
            setBestScoresGCP(data);
        } catch (err) {
            setError('Erreur : ' + err.message);
        }
    };

    const fetchBestScoresWVCP = async () => {
        try {
            const response = await fetch(`${baseUrl}/best_scores_wvcp`);
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des meilleurs scores WVCP.');
            }
            const data = await response.json();
            setBestScoresWVCP(data);
        } catch (err) {
            setError('Erreur : ' + err.message);
        }
    };

    const handleAlgoChange = (event) => {
        setSelectedAlgo(event.target.value);
    };

    const handleLoadGraph = () => {
        if (selectedGraph) {
            fetchGraphColoringData(selectedGraph, selectedAlgo);
        } else {
            setError('Veuillez sélectionner un graphe.');
        }
    };

    useEffect(() => {
        if (showBestScoresGCP) {
            fetchBestScoresGCP();
        }
    }, [showBestScoresGCP]);

    useEffect(() => {
        if (showBestScoresWVCP) {
            fetchBestScoresWVCP();
        }
    }, [showBestScoresWVCP]);

    return (
        <div>
            <h2>Problème de coloration de graphe</h2>

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
                    <option value="basique">Basique</option>
                    <option value="dsatur">DSATUR</option>
                    <option value="random">Random</option>
                </select>

                <button onClick={handleLoadGraph} style={{ padding: '10px' }}>
                    Charger la Coloration du Graphe
                </button>
            </div>

            <div style={{ marginBottom: '20px' }}>
                <label htmlFor="best-scores-gcp" style={{ marginRight: '10px' }}>Afficher les meilleurs scores GCP:</label>
                <input
                    type="checkbox"
                    id="best-scores-gcp"
                    checked={showBestScoresGCP}
                    onChange={(e) => setShowBestScoresGCP(e.target.checked)}
                    style={{ marginRight: '20px' }}
                />

                <label htmlFor="best-scores-wvcp" style={{ marginRight: '10px' }}>Afficher les meilleurs scores WVCP:</label>
                <input
                    type="checkbox"
                    id="best-scores-wvcp"
                    checked={showBestScoresWVCP}
                    onChange={(e) => setShowBestScoresWVCP(e.target.checked)}
                />
            </div>

            {loading && <p>Chargement des données...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {!loading && graphData.nodes.length > 0 && (
                <div style={{ marginBottom: '20px', textAlign: 'center', fontSize: '24px', color: '#FF5733' }}>
                    <strong>Coût total: {cout}</strong>
                </div>
            )}
            {!loading && graphData.nodes.length > 0 && (
                <div style={{ marginBottom: '20px', textAlign: 'center', fontSize: '24px', color: '#FF5733' }}>
                    <strong>Nombre de couleurs utilisées: {numColorsUsed}</strong>
                </div>
            )}
            {!loading && graphData.nodes.length > 0 && (
                <div style={{ marginBottom: '20px', textAlign: 'center', fontSize: '24px', color: '#FF5733' }}>
                    <strong>Temps d'exécution: {executionTime} ms</strong>
                </div>
            )}
            {!loading && graphData.nodes.length > 0 && (
                <GraphView nodes={graphData.nodes} edges={graphData.edges} />
            )}
            {showBestScoresGCP && bestScoresGCP.length > 0 && (
                <div>
                    <h3>Meilleurs scores GCP</h3>
                    <ul>
                        {bestScoresGCP.map((score, index) => (
                            <li key={index}>{score}</li>
                        ))}
                    </ul>
                </div>
            )}
            {showBestScoresWVCP && bestScoresWVCP.length > 0 && (
                <div>
                    <h3>Meilleurs scores WVCP</h3>
                    <ul>
                        {bestScoresWVCP.map((score, index) => (
                            <li key={index}>{score}</li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}

export default GraphColoringProblem;