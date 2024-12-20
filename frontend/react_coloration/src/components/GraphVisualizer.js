import React, { useState } from 'react';
import GraphView from './GraphView';

function GraphVisualizer() {
    const [graphData, setGraphData] = useState({ nodes: [], edges: [] });
    const [backendUrl, setBackendUrl] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const fetchGraphData = async () => {
        setLoading(true);
        setError('');
        try {
            const response = await fetch(backendUrl);
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des données.');
            }
            const data = await response.json();
            setGraphData({
                nodes: data.nodes.map(node => ({
                    ...node,
                    label: node.id.toString(),
                })),
                edges: data.edges || [],
            });
        } catch (err) {
            setError('Erreur : ' + err.message);
        }
        setLoading(false);
    };

    const handleInputChange = (event) => {
        setBackendUrl(event.target.value);
    };

    const handleLoadGraph = () => {
        if (backendUrl) {
            fetchGraphData();
        } else {
            setError('Veuillez saisir une URL valide.');
        }
    };

    return (
        <div>
            <h2>Visualisation du graphe</h2>
            <input
                type="text"
                placeholder="Saisissez l'URL du backend"
                value={backendUrl}
                onChange={handleInputChange}
                style={{ width: '300px', padding: '10px', marginRight: '10px' }}
            />
            <button onClick={handleLoadGraph} style={{ padding: '10px' }}>
                Charger le Graph
            </button>
            {loading && <p>Chargement des données...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {!loading && graphData.nodes.length > 0 && (
                <GraphView nodes={graphData.nodes} edges={graphData.edges} />
            )}
        </div>
    );
}

export default GraphVisualizer;
