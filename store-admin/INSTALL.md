# Installation Guide

## Quick Fix for Module Issues

The "Cannot find module" errors occur because the dependencies aren't installed yet. Follow these steps:

### 1. Install Dependencies

```bash
cd store-admin
npm install
```

### 2. If you still get errors, try:

```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and package-lock.json
rm -rf node_modules package-lock.json

# Reinstall
npm install
```

### 3. Alternative: Use a simpler version first

If you're still having issues with PrimeNG, you can start with a basic HTML version:

```bash
# Create a simple HTML version
cp src/app/pages/dashboard/dashboard.component.ts src/app/pages/dashboard/dashboard.component.ts.backup
```

Then edit the dashboard component to use basic HTML instead of PrimeNG components.

### 4. Verify Installation

After installation, you should see:
- `node_modules/` directory created
- No "Cannot find module" errors in your IDE
- `npm start` should work

### 5. Common Issues

**Windows Users:**
- Make sure you're using PowerShell or Command Prompt as Administrator
- Try running: `npm install --legacy-peer-deps`

**Mac/Linux Users:**
- Make sure you have proper permissions: `sudo npm install`

**Node Version:**
- Ensure you have Node.js 16+ installed: `node --version`

### 6. Start the Application

```bash
npm start
```

The application should be available at `http://localhost:4200`

## Troubleshooting

If you continue to have issues:

1. **Check Node.js version**: `node --version` (should be 16+)
2. **Check npm version**: `npm --version`
3. **Clear all caches**: `npm cache clean --force`
4. **Use yarn instead**: `yarn install`
5. **Check your internet connection** for downloading packages

## Manual Installation

If automatic installation fails, you can manually install each package:

```bash
npm install @angular/core@^17.0.0
npm install @angular/common@^17.0.0
npm install @angular/compiler@^17.0.0
npm install @angular/forms@^17.0.0
npm install @angular/platform-browser@^17.0.0
npm install @angular/platform-browser-dynamic@^17.0.0
npm install @angular/router@^17.0.0
npm install primeng@^17.7.1
npm install primeicons@^6.0.1
npm install rxjs@~7.8.0
npm install tslib@^2.3.0
npm install zone.js@~0.14.2
``` 