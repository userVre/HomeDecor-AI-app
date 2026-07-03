type GenerationStatus = "processing" | "ready" | "failed";

export function resolveGenerationStatus(
  status: string | undefined,
  imageUrl: string,
): GenerationStatus {
  if (status === "failed") {
    return "failed";
  }

  if (imageUrl && imageUrl.length > 0) {
    return "ready";
  }

  if (status === "ready") {
    return "failed";
  }

  return "processing";
}
