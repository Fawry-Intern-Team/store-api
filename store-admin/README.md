# Store Admin Dashboard

A modern Angular admin dashboard built with PrimeNG for managing stores, stocks, and stock history.

## Features

- **Dashboard Overview**: View key metrics including total stores, stock items, and quantities
- **Store Management**: View all store locations with their details
- **Stock Management**: Monitor stock levels across all stores
- **Stock History**: Track all stock changes with timestamps and reasons
- **Modern UI**: Built with PrimeNG components for a professional look
- **Responsive Design**: Works on desktop and mobile devices

## Prerequisites

- Node.js (version 16 or higher)
- npm (comes with Node.js)
- Angular CLI (will be installed automatically)
- Java Spring Boot backend running on localhost:8080

## Installation

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Start the development server**:
   ```bash
   npm start
   ```

3. **Open your browser** and navigate to `http://localhost:4200`

## API Endpoints

The frontend expects the following backend API endpoints to be available:

- `GET /api/store` - Get all stores
- `GET /api/store/{storeId}/stock` - Get stocks for a specific store
- `GET /api/store/{storeId}/history` - Get stock history for a specific store

## Project Structure

```
src/
├── app/
│   ├── models/           # TypeScript interfaces
│   ├── services/         # API service classes
│   ├── pages/           # Page components
│   │   └── dashboard/   # Main dashboard component
│   ├── app.component.ts # Root component
│   ├── app.config.ts    # Application configuration
│   └── app.routes.ts    # Routing configuration
├── styles.scss          # Global styles
└── main.ts             # Application entry point
```

## Available Scripts

- `npm start` - Start the development server
- `npm run build` - Build the application for production
- `npm run test` - Run unit tests

## Technologies Used

- **Angular 17** - Frontend framework
- **PrimeNG** - UI component library
- **TypeScript** - Programming language
- **SCSS** - Styling
- **RxJS** - Reactive programming

## Customization

### Adding New Features

1. Create new components in the `src/app/pages/` directory
2. Add new services in the `src/app/services/` directory
3. Update routing in `src/app/app.routes.ts`
4. Add new models in `src/app/models/` directory

### Styling

- Global styles are in `src/styles.scss`
- Component-specific styles are in the component files
- PrimeNG theme can be changed in `angular.json`

## Troubleshooting

### Common Issues

1. **CORS Errors**: Ensure your backend has CORS configured to allow requests from `http://localhost:4200`

2. **API Connection Issues**: Verify that your Spring Boot backend is running on `http://localhost:8080`

3. **Build Errors**: Make sure all dependencies are installed with `npm install`

### Development Tips

- Use the browser's developer tools to debug API calls
- Check the browser console for any JavaScript errors
- Use Angular DevTools extension for better debugging experience

## Contributing

1. Follow the existing code structure and naming conventions
2. Add proper error handling for API calls
3. Include loading states for better user experience
4. Test your changes thoroughly before committing 