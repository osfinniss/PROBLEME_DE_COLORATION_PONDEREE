import React from 'react';
import Graph from 'react-vis-network-graph';

export default function GraphView({ nodes, edges }) {
    const graph = {
        nodes: nodes || [],
        edges: edges || []
    };

    const options = {
        physics: {
            enabled: false
        },
        interaction: {
            navigationButtons: true
        },
        nodes: {
            borderWidth: 2,
            size: 40,
            font: { color: "black" }
        },
        edges: {
            color: "black",
            arrows: { to: false }
        },
        shadow: true,
        smooth: true,
        height: "900px"
    };

    return (
        <div className='container'>
            <Graph
                graph={graph}
                options={options}
            />
        </div>
    );
}
