import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

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

function GraphColoringTable({ graphData }) {
    const colorGroups = graphData.nodes.reduce((acc, node) => {
        if (!acc[node.couleur]) {
            acc[node.couleur] = [];
        }
        acc[node.couleur].push(node);
        return acc;
    }, {});

    return (
        <div>
            <h2>Tableau de Coloration de Graphe</h2>
            <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around' }}>
                {Object.keys(colorGroups).map(color => (
                    <div key={color} style={{ border: '1px solid black', padding: '10px', maxWidth: '200px', margin: '10px' }}>
                        <h3 style={{ color: colorMapping[color] || '#000000' }}>Couleur {color}</h3>
                        <ul>
                            {colorGroups[color].map(node => (
                                <li key={node.id}>{node.label}</li>
                            ))}
                        </ul>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default GraphColoringTable;