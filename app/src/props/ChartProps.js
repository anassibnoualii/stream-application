import PropTypes from 'prop-types';

const chartItemShape = PropTypes.shape({
  key: PropTypes.string.isRequired,
  metrics: PropTypes.arrayOf(
    PropTypes.shape({
      key: PropTypes.string.isRequired,
      value: PropTypes.string.isRequired,
    })
  ).isRequired,
});

export const chartDataShape = PropTypes.arrayOf(chartItemShape);

