// File: App.js
import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import GraphVisualizer from './components/GraphVisualizer';
import GraphColoringProblem from './components/GraphColoringProblem';
import GraphColoringStepByStep from './components/GraphColoringStepByStep';
import GraphWeightedColoringProblem from './components/GraphWeightedColoringProblem';
import './App.css';

function App() {
    const [activeTab, setActiveTab] = useState('visualization');

    return (
        <Router>
            <div className="App">
                <header className="header">
                    <h1 className="title">Graph Application</h1>
                    <nav>
                        <ul className="menu-container">
                            <li>
                                <Link
                                    to="/coloration"
                                    className={`menu ${activeTab === 'coloration' ? 'active' : ''}`}
                                    onClick={() => setActiveTab('coloration')}
                                >
                                    Problème de coloration de graphe
                                </Link>
                            </li>
                            <li>
                                <Link
                                    to="/step-by-step"
                                    className={`menu ${activeTab === 'step-by-step' ? 'active' : ''}`}
                                    onClick={() => setActiveTab('step-by-step')}
                                >
                                    Problème de coloration de graphe étape par étape
                                </Link>
                            </li>
                            <li>
                                <Link
                                    to="/weighted-coloring"
                                    className={`menu ${activeTab === 'weighted-coloring' ? 'active' : ''}`}
                                    onClick={() => setActiveTab('weighted-coloring')}
                                >
                                    Problème de coloration pondérée: Recherche Locale
                                </Link>
                            </li>
                        </ul>
                    </nav>
                </header>

                <main className="main-content">
                    <Routes>
                        <Route path="/coloration" element={<GraphColoringProblem />} />
                        <Route path="/step-by-step" element={<GraphColoringStepByStep />} />
                        <Route path="/weighted-coloring" element={<GraphWeightedColoringProblem />} />
                    </Routes>
                </main>
            </div>
        </Router>
    );
}

export default App;