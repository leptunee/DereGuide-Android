#!/usr/bin/env bash
set -e
source $(dirname "$0")/config.sh

if [ ! -d "${TARGET_DIR}" ]; then
    echo "The translations directory is missing. Run 'export.sh' first."
    exit 1
fi

echo "Importing translations..."
for language in "${TRANSLATIONS[@]}"; do
    xcodebuild -importLocalizations -localizationPath "${TARGET_DIR}/${language}.xcloc/Localized Contents/${language}.xliff" \
        -project "${PROJECT_FILE_PATH}"
done
echo "Done."
