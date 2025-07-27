# Troubleshooting Guide

## Error: "Cannot find module '@angular/platform-browser/animations'"

This error occurs when there are missing or incompatible Angular dependencies.

### Solution 1: Clean Install

```bash
# Remove existing node_modules and package-lock.json
rm -rf node_modules package-lock.json

# Clear npm cache
npm cache clean --force

# Reinstall dependencies
npm install
```

### Solution 2: Use Minimal Dependencies

If the above doesn't work, use the minimal package.json:

```bash
# Backup current package.json
cp package.json package.json.backup

# Use minimal version
cp package.json.minimal package.json

# Install minimal dependencies
npm install
```

### Solution 3: Manual Installation

Install each package individually:

```bash
npm install @angular/core@^17.0.0
npm install @angular/common@^17.0.0
npm install @angular/compiler@^17.0.0
npm install @angular/forms@^17.0.0
npm install @angular/platform-browser@^17.0.0
npm install @angular/platform-browser-dynamic@^17.0.0
npm install @angular/router@^17.0.0
npm install @angular/animations@^17.0.0
npm install rxjs@~7.8.0
npm install tslib@^2.3.0
npm install zone.js@~0.14.2
```

### Solution 4: Check Node.js Version

Ensure you have the correct Node.js version:

```bash
node --version  # Should be 16+ for Angular 17
npm --version   # Should be 8+
```

### Solution 5: Use Yarn Instead

If npm continues to have issues:

```bash
# Install yarn if not installed
npm install -g yarn

# Install dependencies with yarn
yarn install
```

### Solution 6: Angular CLI Issues

If you're still having issues, try:

```bash
# Install Angular CLI globally
npm install -g @angular/cli@17

# Create a new project and copy files
ng new temp-project
# Copy your source files to the new project
```

## Current Configuration

The app is currently configured to use:
- Simple app configuration (no animations)
- Basic HTML dashboard (no PrimeNG)
- Minimal dependencies

## Start the Application

After fixing the dependencies:

```bash
npm start
```

The application should be available at `http://localhost:4200`

## Add PrimeNG Later

Once the basic app is working, you can add PrimeNG:

```bash
npm install primeng@^17.7.1 primeicons@^6.0.1
```

Then update the routing to use the PrimeNG dashboard component. 