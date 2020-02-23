module.exports = {
  entry: {
    'okta-auth-js': './main.js'
  },
  output: {
    filename: '[name].inc.js'
  },
  externals: {
    'react': 'React',
    'react-dom': 'ReactDOM'
  }
};
