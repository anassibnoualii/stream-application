import React, {useEffect, useState} from 'react';
import {Bar} from 'react-chartjs-2';
import {Chart, registerables} from "chart.js";
import './App.css'
import {chartDataShape} from "./props/ChartProps";
import {API_URL} from "./Constants";

Chart.register(...registerables);

const ChartCard = ({chartData}) => {
  ChartCard.prototype = {
    chartData: chartDataShape.isRequired,
  }
  const data = {
    labels: chartData.metrics.map(i => i.key),
    datasets: [
      {
        label: chartData.key,
        data: chartData.metrics.map(i => i.value),
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
      },
    ],
  };

  const options = {
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  };

  return (
      <div className="chartCard">
        <div className="chartBox">
          <Bar data={data} options={options}/>
        </div>
      </div>
  );
};

const App = () => {
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);

  function loadChartData() {
    fetch(API_URL)
    .then((response) => response.json())
    .then((data) => {
      setChartData(data);
      setLoading(false);
    })
    .catch((error) => {
      console.error('Error fetching data:', error);
      setLoading(false);
    });
  }

  useEffect(() => {
    loadChartData();
  }, []);

  return (
      !loading ?
          <div>
            <div className="chartMenu">
              <p>Stream charts</p>
              <button onClick={() => loadChartData()}>Refresh Data</button>
            </div>

            <div className="chartRow">
              {chartData.map((chartItem, index) => (
                  <ChartCard key={index} chartData={chartItem}/>))}
            </div>
          </div> : ''
  );
};

export default App;
