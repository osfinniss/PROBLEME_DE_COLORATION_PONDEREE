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

function GraphColoringStepByStep() {
    const [graphData, setGraphData] = useState({ nodes: [], edges: [] });
    const [nodePositions, setNodePositions] = useState({});
    const [customGraph, setCustomGraph] = useState('GEOM30');
    const [selectedAlgo, setSelectedAlgo] = useState('glouton');
    const [step, setStep] = useState(0);
    const [totalSteps, setTotalSteps] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [changedNodes, setChangedNodes] = useState([]);

    const baseUrl = 'http://localhost:8080/api/colorationEtape';

    const fetchGraphColoringStepData = async (graph, algo, step) => {
        setLoading(true);
        setError('');
        try {
            const response = await fetch(`${baseUrl}/${graph}/${algo}`);
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des données.');
            }
            const data = await response.json();
            const stepData = data[step];

            const updatedNodes = stepData.nodes.map(node => ({
                ...node,
                label: node.id.toString() + "(" + node.poids.toString() + ")",
                color: colorMapping[node.couleur] || '#000000',
                x: nodePositions[node.id]?.x || Math.random() * 1000,
                y: nodePositions[node.id]?.y || Math.random() * 1000
            }));

            const newPositions = { ...nodePositions };
            updatedNodes.forEach(node => {
                if (!newPositions[node.id]) {
                    newPositions[node.id] = { x: node.x, y: node.y };
                }
            });
            setNodePositions(newPositions);

            setGraphData({
                nodes: updatedNodes,
                edges: stepData.edges || [],
            });

            setTotalSteps(data.length || 0);
        } catch (err) {
            setError('Erreur : ' + err.message);
        }
        setLoading(false);
    };

    const handleAlgoChange = (event) => {
        setSelectedAlgo(event.target.value);
    };

    const handleGraphChange = (event) => {
        setCustomGraph(event.target.value);
    };

    const handleNextStep = () => {
        if (step < totalSteps) {
            updateChangedNodes(step + 1);
            const nextStep = step + 1;
            setStep(nextStep);
            fetchGraphColoringStepData(customGraph, selectedAlgo, nextStep);
        }
    };

    const handlePreviousStep = () => {
        if (step > 1) {
            updateChangedNodes(step - 1, true);
            const previousStep = step - 1;
            setStep(previousStep);
            fetchGraphColoringStepData(customGraph, selectedAlgo, previousStep);
        }
    };

    const handleLoadFirstStep = () => {
        setStep(1);
        fetchGraphColoringStepData(customGraph, selectedAlgo, 1);
        setChangedNodes([]);
    };

    const updateChangedNodes = (step, isPrevious = false) => {
        fetch(`${baseUrl}/${customGraph}/${selectedAlgo}`)
            .then(response => response.json())
            .then(data => {
                const stepData = data[step];
                const changedNodesInStep = stepData.nodes.filter(node => node.couleur !== graphData.nodes.find(n => n.id === node.id)?.couleur);
                if (isPrevious) {
                    setChangedNodes(prev => prev.filter(node => !changedNodesInStep.some(n => n.id === node.id)));
                } else {
                    setChangedNodes(prev => [...prev, ...changedNodesInStep]);
                }
            })
            .catch(err => setError('Erreur : ' + err.message));
    };

    return (
        <div>
            <h2>Problème de coloration de graphe étape par étape</h2>

            <div style={{ marginBottom: '20px' }}>
                <label htmlFor="graph-select" style={{ marginRight: '10px' }}>Choisissez un graphe:</label>
                <select
                    id="graph-select"
                    value={customGraph}
                    onChange={handleGraphChange}
                    style={{ padding: '10px', marginRight: '10px' }}
                >
                    <option value="GEOM30">GEOM30</option>
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

                <button onClick={handleLoadFirstStep} style={{ padding: '10px' }}>
                    Charger la première étape
                </button>
            </div>

            {loading && <p>Chargement des données...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {!loading && graphData.nodes.length > 0 && (
                <div style={{ display: 'flex' }}>
                    <GraphView nodes={graphData.nodes} edges={graphData.edges} />
                    <div style={{ marginLeft: '20px' }}>
                        <h3>Sommets dont la couleur a changé</h3>
                        <table border="1">
                            <thead>
                            <tr>
                                {Object.keys(colorMapping).map(colorKey => (
                                    <th key={colorKey} style={{ backgroundColor: colorMapping[colorKey] }}>
                                        {colorKey}
                                    </th>
                                ))}
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                {Object.keys(colorMapping).map(colorKey => (
                                    <td key={colorKey}>
                                        {changedNodes.filter(node => node.couleur === parseInt(colorKey)).map(node => (
                                            <div key={node.id}>{node.id}</div>
                                        ))}
                                    </td>
                                ))}
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
            <div style={{ marginTop: '20px' }}>
                <button
                    onClick={handlePreviousStep}
                    style={{ padding: '10px', marginRight: '10px' }}
                    disabled={step <= 1}
                >
                    Précédent
                </button>
                <button
                    onClick={handleNextStep}
                    style={{ padding: '10px' }}
                    disabled={step >= totalSteps}
                >
                    Suivant
                </button>
            </div>
            <p>Étape {step} sur {totalSteps}</p>
        </div>
    );
}

export default GraphColoringStepByStep;